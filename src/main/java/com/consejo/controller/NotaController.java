package com.consejo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.consejo.daos.NotaDaos;
import com.consejo.pojos.Nota;

@Controller
public class NotaController {


    @Autowired
    private NotaDaos notaServi;
    

    @GetMapping("/administrador/nota")
    public String getAllNotas(Model model) {
        model.addAttribute("notas", notaServi.listarNota());
        return "notas";
    }

    @GetMapping("/notas/upload")
    public String showUploadForm() {
        return "uploadNota";
    }

    @PostMapping("/notas/upload")
    public String uploadNota(@RequestParam("titulo") String titulo, @RequestParam("file") MultipartFile file, Model model) throws IOException {
        try {
        	Nota nota = new Nota();
            byte[] notaBytes = file.getBytes();
            nota.setNota(notaBytes);
            nota.setTitulo(titulo);
            notaServi.guardarNota(nota );
            return "redirect:/notas/success";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/notas/error";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CONCEJAL', 'ROLE_PRESIDENTE')")
    @GetMapping("/notas/view/{id}")
    public ResponseEntity<byte[]> viewNota(@PathVariable Long id) {
        Nota nota = notaServi.BuscarNota(id);
        byte[] pdfContent = nota.getNota(); // Obtén el contenido del PDF desde tu base de datos

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }

    @GetMapping("/notas/download/{id}")
    public ResponseEntity<byte[]> downloadNota(@PathVariable Long id) {
        Nota nota = notaServi.BuscarNota(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nota.getTitulo() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(nota.getNota());
    }
    
    @GetMapping("/notas/success")
    public String successPage() {

        return "success";
    }
}
