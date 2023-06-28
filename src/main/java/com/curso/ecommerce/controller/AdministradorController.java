package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordensService;
	
	private Logger logg= LoggerFactory.getLogger(AdministradorController.class);

	@GetMapping("")
	public String home(Model model) {

		List<Producto> productos = productoService.findAll();
		model.addAttribute("productos", productos);
		return "administrador/home";
	}
	
	
	@GetMapping("{categoria}")
	public String home(Model model, HttpSession session, @PathVariable("categoria") String categoria) {		
		
		//logg.info("Sesion del usuario: {}", session.getAttribute("idusuario"));		
		//model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		List<Producto> productosFiltrados = new ArrayList<>();
		if(categoria == null || categoria.isBlank() || categoria.isEmpty()) {
			model.addAttribute("productos", productoService.findAll());
		} else {
			for (Producto producto : productoService.findAll()) {
				if (producto.getCategoria().equals(categoria)) {
					productosFiltrados.add(producto);
				}
			}
			model.addAttribute("productos", productosFiltrados);
		}
		return "administrador/home";
	}
	
	
	@GetMapping("/usuarios")
	public String usuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.findAll());
		return "administrador/usuarios";
	}
	
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		model.addAttribute("ordenes", ordensService.findAll());
		return "administrador/ordenes";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalle(Model model, @PathVariable Integer id) {
		logg.info("Id de la orden {}",id);
		Orden orden= ordensService.findById(id).get();
		
		model.addAttribute("detalles", orden.getDetalle());
		
		return "administrador/detalleorden";
	}
	
	@PostMapping("/searchuser")
	public String searchUser(@RequestParam String nombre, Model model) {
		logg.info("Nombre de usuario: {}", nombre);
		List<Usuario> usuario= usuarioService.findAll().stream().filter( p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
		model.addAttribute("usuarios", usuario);		
		return "administrador/usuarios";
	}
	
	@PostMapping("/searchorder")
	public String searchOrder(@RequestParam String numero, Model model) {
		logg.info("Numero de orden: {}", numero);
		List<Orden> orden = ordensService.findAll().stream().filter(o -> o.getNumero().contains(numero)).collect(Collectors.toList());
		model.addAttribute("ordenes", orden);		
		return "administrador/ordenes";
	}
}
