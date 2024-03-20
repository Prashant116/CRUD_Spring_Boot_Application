package com.storeSite.storeSite.controllers;

import com.storeSite.storeSite.model.Product;
import com.storeSite.storeSite.model.ProductDto;
import com.storeSite.storeSite.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    ProductService productService;

    @GetMapping({"", "/"})
    public String getALlProducts(Model model){
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model){
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String addProduct(
        @Valid @ModelAttribute ProductDto productDto,
        BindingResult result){

        if(productDto.getImageFile().isEmpty()){
            result.addError(new FieldError("productDto", "imageFile", "Image file is required."));
        }

        if(result.hasErrors()){
            return "products/CreateProduct";
        }

        //save image file
        MultipartFile image = productDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
        //System.out.println(storageFileName);

        try{
            String uploadDir = "public/images/";
            java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            try(InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream, Paths.get(uploadDir+storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }

        //create product to be uploaded to the database
        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCatagory(productDto.getCatagory());
        product.setPrice(productDto.getPrice());
        product.setImageFileName(storageFileName);
        product.setCreatedAt(createdAt);
        product.setDescription(productDto.getDescription());

        productService.addProduct(product);


        return "redirect:/products";
    }

    //handle update request
    @GetMapping("/edit")
    public String updateProduct(@RequestParam int id, Model model){
        try{
            Product product = productService.selectProduct(id);
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCatagory(product.getCatagory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());

            model.addAttribute("productDto", productDto);
        }
        catch(Exception e){
            System.out.println("Exception: " +e);
            return "products";
        }
        return "products/editProduct";
    }

    @PostMapping("/edit")
    public String updateProduct(Model model,
                                @Valid @ModelAttribute ProductDto productDto,
                                @RequestParam int id,
                                BindingResult result
                                ){
        try{
            Product product = productService.selectProduct(id);
            model.addAttribute("product", product);

            if(result.hasErrors()){
                return "products/editProduct";
            }

            if(!productDto.getImageFile().isEmpty()){
                //delete old image
                String uploadDir = "public/images/";
                Path oldUploadPath = Paths.get(uploadDir + product.getImageFileName());

                try{
                    Files.delete(oldUploadPath);
                }
                catch(Exception e){
                    System.out.println("Exception: " + e);
                }

                //save new image
                MultipartFile image = productDto.getImageFile();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try(InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                }

                product.setImageFileName(storageFileName);

            }
            product.setCatagory(productDto.getCatagory());
            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            productService.addProduct(product);
        }
        catch(Exception e){
            System.out.println("Exception: e");
        }
        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id){
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
