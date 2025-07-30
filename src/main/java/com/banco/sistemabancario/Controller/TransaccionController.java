
package com.banco.sistemabancario.Controller;

import java.net.http.HttpClient;

import com.banco.sistemabancario.Service.TransaccionService;

@Controller
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;


    @Post
    public String depositarDinero(@RequestParam String valor, HttpSession session){

        try {

            Integer idPersona = (Integer) session.getAttribute("idPersona");
            transaccionService.depositar(idPersona, valor);

            return "redirect:/index.html";

        } catch (NoSuchElementException e) {
            System.out.print("Error al depositar: " + e);
            return "redirect:/index.html?error";
        }
        

    }
}