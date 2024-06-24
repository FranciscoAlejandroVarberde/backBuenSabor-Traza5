package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.facade.PedidoFacade;
import com.entidades.buenSabor.business.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/mail")
public class EmailSendController {
    private EmailService emailService;
    private PedidoFacade pedidoFacade;

    public EmailSendController(EmailService emailService, PedidoFacade pedidoFacade) {
        this.emailService = emailService;
        this.pedidoFacade = pedidoFacade;
    }

    @PostMapping("/enviarFactura/{pedidoId}")
    @PreAuthorize("hasAnyAuthority('CAJERO', 'ADMIN', 'SUPERADMIN')")
    public ResponseEntity<String> enviarFacturaPorCorreo(@PathVariable Long pedidoId, @RequestParam String to) {
        try {
            // Generar el PDF de la factura
            try (ByteArrayOutputStream outputStream = pedidoFacade.generatePedidoPDF(pedidoId)) {
                byte[] pdfData = outputStream.toByteArray();

                // Crear el MultipartFile a partir del PDF
                MultipartFile pdfFile = new CustomMultipartFile("factura.pdf", "application/pdf", pdfData);

                // Enviar el correo con el PDF adjunto
                emailService.sendMail(new MultipartFile[] { pdfFile }, to, new String[0], "Factura de su pedido"
                        , "Adjuntamos la factura de su pedido."); }

            return ResponseEntity.ok("Factura enviada por correo correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar la factura por correo");
        }
    }

    @PostMapping("/send")
    public String sendMail(@RequestParam(value = "file", required = false) MultipartFile[] file, String to, String[] cc, String subject, String body) {
        return emailService.sendMail(file, to, cc, subject, body);
    }

    private static class CustomMultipartFile implements MultipartFile {
        private final String name;
        private final String contentType;
        private final byte[] data;

        public CustomMultipartFile(String name, String contentType, byte[] data) {
            this.name = name;
            this.contentType = contentType;
            this.data = data;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getOriginalFilename() {
            return this.name;
        }

        @Override
        public String getContentType() {
            return this.contentType;
        }

        @Override
        public boolean isEmpty() {
            return this.data.length == 0;
        }

        @Override
        public long getSize() {
            return this.data.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return this.data;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(this.data);
        }

        @Override
        public void transferTo(java.io.File destination) throws IOException, IllegalStateException {
            throw new UnsupportedOperationException("This method is not supported.");
        }
    }
}
