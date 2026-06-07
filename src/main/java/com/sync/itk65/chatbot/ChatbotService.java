package com.sync.itk65.chatbot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Service gọi Google Gemini REST API để tạo phản hồi AI.
 *
 * Sử dụng java.net.http.HttpClient (Java 11+) — không cần thêm dependency.
 * Cơ chế RAG: contextData (dữ liệu thực từ DB) được nhúng vào system prompt.
 */
@Service
public class ChatbotService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.0-flash}")
    private String model;

    private static final String GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    // =========================================================
    // SYSTEM PROMPTS THEO VAI TRÒ
    // =========================================================

    private static final String SYSTEM_PROMPT_CUDAN = """
            Bạn là Trợ lý AI thông minh của Hệ thống Quản lý Chung cư SYNC.
            Tên của bạn là "SYNC Assistant".

            NHIỆM VỤ CỦA BẠN:
            1. Trả lời câu hỏi về hóa đơn của CƯ DÂN dựa trên dữ liệu thực được cung cấp.
            2. Cung cấp thông tin về phương tiện đăng ký, kiện hàng/bưu phẩm, phản ánh, yêu cầu dịch vụ và khách thăm của cư dân dựa trên dữ liệu thực tế được cung cấp.
            3. Cung cấp thông tin về thông báo chung cư và cẩm nang cư dân.
            4. Hỗ trợ cư dân tra cứu lịch sử thanh toán, các khoản cần đóng.

            QUY TẮC BẮT BUỘC:
            - Chỉ trả lời các câu hỏi LIÊN QUAN đến: hóa đơn, thanh toán, thông báo chung cư, cẩm nang, quy định, tiện ích, phương tiện, kiện hàng, phản ánh, đặt dịch vụ và khách thăm của căn hộ cư dân đang đăng nhập.
            - Nếu câu hỏi KHÔNG liên quan đến quản lý chung cư (ví dụ: hỏi về thời tiết, lập trình, tin tức...), hãy từ chối lịch sự.
            - KHÔNG tiết lộ dữ liệu của căn hộ khác. Chỉ trả lời về căn hộ của cư dân này.
            - Trả lời bằng tiếng Việt, thân thiện, rõ ràng.
            - Khi nói về tiền, định dạng: X.XXX.XXX VNĐ (có dấu phẩy ngăn cách hàng nghìn).
            - Nếu không tìm thấy thông tin trong dữ liệu, hãy nói rõ là không có dữ liệu thay vì bịa ra.

            NGÀY HIỆN TẠI: %s

            DỮ LIỆU THỰC TẾ CỦA CƯ DÂN:
            %s
            """;

    private static final String SYSTEM_PROMPT_ADMIN = """
            Bạn là Trợ lý AI thông minh của Ban Quản Trị Chung cư SYNC.
            Tên của bạn là "SYNC Admin Assistant".

            NHIỆM VỤ CỦA BẠN:
            1. Hỗ trợ Ban Quản Trị phân tích dữ liệu hóa đơn, thống kê hệ thống (căn hộ, hóa đơn, phương tiện, kiện hàng, phản ánh, dịch vụ, khách thăm).
            2. Trả lời câu hỏi về tỷ lệ căn hộ, doanh thu, hóa đơn quá hạn.
            3. Tổng hợp và phân tích xu hướng từ dữ liệu được cung cấp.

            QUY TẮC BẮT BUỘC:
            - Chỉ trả lời các câu hỏi LIÊN QUAN đến: quản lý chung cư, thống kê, hóa đơn, căn hộ, cư dân, doanh thu, thông báo, phương tiện, kiện hàng, phản ánh, dịch vụ, khách thăm.
            - Nếu câu hỏi KHÔNG liên quan, từ chối lịch sự.
            - Trả lời bằng tiếng Việt, chuyên nghiệp, súc tích.
            - Khi nói về tiền, định dạng: X.XXX.XXX VNĐ.
            - Có thể đưa ra nhận xét, phân tích xu hướng dựa trên dữ liệu.
            - Nếu không có dữ liệu, hãy nói rõ thay vì bịa.

            NGÀY HIỆN TẠI: %s

            DỮ LIỆU THỐNG KÊ HỆ THỐNG:
            %s
            """;

    private static final String SYSTEM_PROMPT_LETAN = """
            Bạn là Trợ lý AI của Lễ tân Chung cư SYNC.
            Tên của bạn là "SYNC Reception Assistant".

            NHIỆM VỤ CỦA BẠN:
            1. Hỗ trợ Lễ tân theo dõi các kiện hàng/bưu phẩm đang chờ nhận.
            2. Tra cứu và quản lý các yêu cầu đặt dịch vụ của cư dân đang chờ duyệt.
            3. Theo dõi các phản ánh/khiếu nại của cư dân đang chờ xử lý hoặc đang giải quyết.
            4. Hỗ trợ tra cứu thông tin đăng ký khách thăm gần đây để phục vụ công tác tiếp đón.

            QUY TẮC BẮT BUỘC:
            - Chỉ trả lời các câu hỏi LIÊN QUAN đến: công việc lễ tân, kiện hàng, phản ánh, đơn đặt dịch vụ, đăng ký khách thăm, thông báo chung cư.
            - Trả lời bằng tiếng Việt lịch sự, chu đáo, chuyên nghiệp.
            - Nếu không tìm thấy thông tin trong dữ liệu được cung cấp, hãy báo là chưa có thông tin thay vì tự bịa ra.

            NGÀY HIỆN TẠI: %s

            DỮ LIỆU CÔNG VIỆC CỦA LỄ TÂN:
            %s
            """;

    private static final String SYSTEM_PROMPT_BAOVE = """
            Bạn là Trợ lý AI của Bảo vệ Chung cư SYNC.
            Tên của bạn là "SYNC Security Assistant".

            NHIỆM VỤ CỦA BẠN:
            1. Hỗ trợ Bảo vệ tra cứu thông tin phương tiện đăng ký (xe máy, ô tô, xe đạp) của chung cư.
            2. Khi bảo vệ hỏi về một BIỂN SỐ XE cụ thể (ví dụ: "29A-123.45 là của ai?"), hãy tra cứu nhanh danh sách phương tiện để trả ra thông tin: Biển số đó thuộc căn hộ nào, chủ hộ là ai, loại xe gì, màu sắc và trạng thái đã duyệt hay chưa.
            3. Hỗ trợ tra cứu danh sách đăng ký khách thăm gần đây hoặc sắp tới (bao gồm thông tin tên khách, CMND, biển số xe khách, căn hộ đến thăm, thời gian) để kiểm soát ra vào.

            QUY TẮC BẮT BUỘC:
            - Chỉ trả lời các câu hỏi LIÊN QUAN đến: kiểm soát an ninh, biển số xe, phương tiện đăng ký, đăng ký khách thăm ra vào tòa nhà, các thông báo khẩn cấp (nếu có).
            - Trả lời bằng tiếng Việt ngắn gọn, rõ ràng, dứt khoát, đi thẳng vào vấn đề chính.
            - Nếu không tìm thấy biển số xe hoặc thông tin khách thăm trong dữ liệu, hãy báo rõ: "Không tìm thấy thông tin biển số xe/khách thăm này trên hệ thống." thay vì tự bịa ra thông tin.

            NGÀY HIỆN TẠI: %s

            DỮ LIỆU CÔNG VIỆC CỦA BẢO VỆ:
            %s
            """;

    // =========================================================
    // MAIN METHOD
    // =========================================================

    /**
     * Gọi Gemini API và trả về phản hồi dạng text.
     *
     * @param userMessage Tin nhắn từ người dùng
     * @param contextData Dữ liệu ngữ cảnh từ DB (được inject vào system prompt)
     * @param vaiTro      Vai trò người dùng (3=cư dân, 4=lễ tân, 5=bảo vệ, 1/2=admin)
     * @return Phản hồi text từ AI
     */
    public String chat(String userMessage, String contextData, int vaiTro) {
        try {
            String today = LocalDate.now().toString();
            String systemPrompt;
            if (vaiTro == 3) {
                systemPrompt = String.format(SYSTEM_PROMPT_CUDAN, today, contextData);
            } else if (vaiTro == 4) {
                systemPrompt = String.format(SYSTEM_PROMPT_LETAN, today, contextData);
            } else if (vaiTro == 5) {
                systemPrompt = String.format(SYSTEM_PROMPT_BAOVE, today, contextData);
            } else {
                systemPrompt = String.format(SYSTEM_PROMPT_ADMIN, today, contextData);
            }

            // Xây dựng JSON body theo Gemini REST API format
            // https://ai.google.dev/api/generate-content
            ObjectNode body = objectMapper.createObjectNode();

            // System instruction
            ObjectNode systemInstruction = objectMapper.createObjectNode();
            ObjectNode systemPart = objectMapper.createObjectNode();
            systemPart.put("text", systemPrompt);
            ArrayNode systemParts = objectMapper.createArrayNode();
            systemParts.add(systemPart);
            systemInstruction.set("parts", systemParts);
            body.set("systemInstruction", systemInstruction);

            // User message
            ObjectNode userPart = objectMapper.createObjectNode();
            userPart.put("text", userMessage);
            ArrayNode userParts = objectMapper.createArrayNode();
            userParts.add(userPart);
            ObjectNode userContent = objectMapper.createObjectNode();
            userContent.put("role", "user");
            userContent.set("parts", userParts);
            ArrayNode contents = objectMapper.createArrayNode();
            contents.add(userContent);
            body.set("contents", contents);

            // Generation config — giới hạn token để tránh phản hồi quá dài
            ObjectNode generationConfig = objectMapper.createObjectNode();
            generationConfig.put("maxOutputTokens", 1024);
            generationConfig.put("temperature", 0.3);
            body.set("generationConfig", generationConfig);

            // Safety settings - tắt các filter không cần thiết cho use case này
            ArrayNode safetySettings = objectMapper.createArrayNode();
            String[] harmCategories = {
                    "HARM_CATEGORY_HARASSMENT",
                    "HARM_CATEGORY_HATE_SPEECH",
                    "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                    "HARM_CATEGORY_DANGEROUS_CONTENT"
            };
            for (String cat : harmCategories) {
                ObjectNode setting = objectMapper.createObjectNode();
                setting.put("category", cat);
                setting.put("threshold", "BLOCK_NONE");
                safetySettings.add(setting);
            }
            body.set("safetySettings", safetySettings);

            String requestBody = objectMapper.writeValueAsString(body);
            String url = String.format(GEMINI_BASE_URL, model, apiKey);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseGeminiResponse(response.body());
            } else {
                System.err
                        .println("[ChatbotService] Gemini API error " + response.statusCode() + ": " + response.body());
                return "Xin lỗi, có lỗi kết nối tới dịch vụ AI. Vui lòng thử lại sau.";
            }

        } catch (Exception e) {
            System.err.println("[ChatbotService] Exception: " + e.getMessage());
            return "Xin lỗi, hệ thống AI tạm thời không khả dụng. Vui lòng thử lại sau ít phút.";
        }
    }

    // =========================================================
    // PARSE RESPONSE
    // =========================================================

    /**
     * Trích xuất text phản hồi từ JSON response của Gemini API.
     */
    private String parseGeminiResponse(String jsonBody) {
        try {
            JsonNode root = objectMapper.readTree(jsonBody);
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode firstCandidate = candidates.get(0);
                // Kiểm tra finish reason
                String finishReason = firstCandidate.path("finishReason").asText("");
                if ("SAFETY".equals(finishReason)) {
                    return "Xin lỗi, tôi không thể xử lý yêu cầu này. Vui lòng đặt câu hỏi theo cách khác.";
                }
                JsonNode parts = firstCandidate.path("content").path("parts");
                if (parts.isArray() && !parts.isEmpty()) {
                    return parts.get(0).path("text").asText("Không có phản hồi từ AI.");
                }
            }
            // Kiểm tra error từ API
            JsonNode error = root.path("error");
            if (!error.isMissingNode()) {
                String errMsg = error.path("message").asText("Lỗi không xác định");
                System.err.println("[ChatbotService] Gemini API returned error: " + errMsg);
                return "Xin lỗi, dịch vụ AI gặp lỗi: " + errMsg;
            }
            return "Xin lỗi, không thể đọc phản hồi từ AI.";
        } catch (Exception e) {
            System.err.println("[ChatbotService] Failed to parse response: " + e.getMessage());
            return "Xin lỗi, có lỗi xử lý phản hồi từ AI.";
        }
    }
}
