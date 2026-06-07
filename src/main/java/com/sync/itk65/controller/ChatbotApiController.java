package com.sync.itk65.controller;

import com.sync.itk65.chatbot.ChatbotContextService;
import com.sync.itk65.chatbot.ChatbotService;
import com.sync.itk65.chatbot.dto.ChatRequest;
import com.sync.itk65.chatbot.dto.ChatResponse;
import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.repository.CuDanRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller xử lý API Chatbot.
 *
 * Endpoint: POST /api/chatbot/chat
 *
 * Luồng xử lý:
 * 1. Lấy NguoiDung từ session → xác định vai trò
 * 2. Nếu là CuDan: lấy canHo → buildCuDanContext()
 * 3. Nếu là Admin/QL/LT/BV: buildAdminContext()
 * 4. Gọi ChatbotService.chat() → trả về ChatResponse JSON
 *
 * Bảo mật: Endpoint này được bảo vệ bởi AuthInterceptor (xem WebConfig).
 * Cư dân chỉ có thể xem dữ liệu của canHo của chính mình.
 */
@RestController
@RequestMapping("/api/chatbot")
public class ChatbotApiController {

    @Autowired
    private ChatbotService chatbotService;

    @Autowired
    private ChatbotContextService chatbotContextService;

    @Autowired
    private CuDanRepository cuDanRepository;

    /**
     * Endpoint nhận tin nhắn và trả về phản hồi AI.
     *
     * @param request  Body JSON: { "message": "..." }
     * @param session  HTTP session (để lấy thông tin người dùng đăng nhập)
     * @return JSON: { "reply": "...", "success": true/false, "error": "..." }
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(
            @RequestBody ChatRequest request,
            HttpSession session) {

        // Lấy người dùng từ session
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (nguoiDung == null) {
            return ResponseEntity.status(401)
                    .body(ChatResponse.fail("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại."));
        }

        // Validate input
        String userMessage = request.getMessage();
        if (userMessage == null || userMessage.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ChatResponse.fail("Tin nhắn không được để trống."));
        }
        if (userMessage.length() > 1000) {
            return ResponseEntity.badRequest()
                    .body(ChatResponse.fail("Tin nhắn quá dài (tối đa 1000 ký tự)."));
        }

        int vaiTro = nguoiDung.getVaiTro();
        String contextData;

        try {
            if (vaiTro == 3) {
                // === CƯ DÂN ===
                // Tìm thông tin CuDan (bao gồm canHo) để build context
                CuDan cuDan = cuDanRepository.findById(nguoiDung.getId()).orElse(null);
                if (cuDan == null) {
                    return ResponseEntity.badRequest()
                            .body(ChatResponse.fail("Không tìm thấy thông tin cư dân. Vui lòng liên hệ BQL."));
                }

                CanHo canHo = cuDan.getCanHo();
                if (canHo == null) {
                    // Cư dân chưa được gán căn hộ — chỉ cho xem thông báo
                    contextData = "Cư dân này chưa được gán vào căn hộ nào. Chỉ có thể xem thông báo chung (ALL).";
                } else {
                    contextData = chatbotContextService.buildCuDanContext(
                            canHo.getId(),
                            canHo.getMaCanHo(),
                            canHo.getTang(),
                            cuDan.getId()
                    );
                }
            } else if (vaiTro == 4) {
                // === LỄ TÂN ===
                contextData = chatbotContextService.buildLeTanContext();
            } else if (vaiTro == 5) {
                // === BẢO VỆ ===
                contextData = chatbotContextService.buildBaoVeContext();
            } else {
                // === ADMIN / QUẢN LÝ ===
                contextData = chatbotContextService.buildAdminContext();
            }

            // Gọi Gemini AI
            String aiReply = chatbotService.chat(userMessage, contextData, vaiTro);
            return ResponseEntity.ok(ChatResponse.ok(aiReply));

        } catch (Exception e) {
            System.err.println("[ChatbotApiController] Error processing chat: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ChatResponse.fail("Đã xảy ra lỗi xử lý. Vui lòng thử lại."));
        }
    }
}
