package com.stage.appToDo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository repo;

    /** Soumission du formulaire du modal  */
    @PostMapping("/add")
    public RedirectView addCategory(@RequestParam String name) {
        Category c = new Category();
        c.setName(name.trim());
        repo.save(c);
        // retourne vers le formulaire tâche après création
        return new RedirectView("/tasks/new");
    }



    /** JSON utilisé par le JS (optionnel) */
    @GetMapping("/all")
    @ResponseBody
    public java.util.List<Category> all() {
        return repo.findAll();
    }
}
