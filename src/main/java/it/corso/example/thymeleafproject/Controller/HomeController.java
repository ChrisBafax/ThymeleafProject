package it.corso.example.thymeleafproject.Controller;

import it.corso.example.thymeleafproject.business.interfaces.AssetBO;
import it.corso.example.thymeleafproject.business.interfaces.CategoryBO;
import it.corso.example.thymeleafproject.model.Asset;
import it.corso.example.thymeleafproject.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    AssetBO assetBO;
    @Autowired
    CategoryBO categoryBO;

    @GetMapping(value = {"/index","/","/home"})
    public String getAll(Model model,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "3") int size,
                         @RequestParam(defaultValue = "1") long id) {
        List<Asset> assets = new ArrayList<Asset>();
        Pageable paging = PageRequest.of(page - 1, size);
        Page<Asset> pageAssets;

        pageAssets = assetBO.findAll(paging);
        assets = pageAssets.getContent();
        model.addAttribute("assets", assets);
        model.addAttribute("currentPage", pageAssets.getNumber() + 1);
        model.addAttribute("totalItems", pageAssets.getTotalElements());
        model.addAttribute("totalPages", pageAssets.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("BTC_Value",assetBO.getCurrentValue());
        model.addAttribute("categories",categoryBO.getAllCategory());
        return "main.html";
    }

    @GetMapping(value = {"/createCrypto", "/newCrypto"})
    public String createCryptoPage() {
        return "createCrypto.html";
    }

    @PostMapping("/createCrypto")
    public String createCrypto(@RequestParam String name,
                               @RequestParam String amount,
                               @RequestParam String description,
                               @RequestParam String url) {
        Category category = categoryBO.getCategory(Long.parseLong("1"));

        Asset asset = new Asset();
        asset.setName(name);
        asset.setAmount(Long.parseLong(amount));
        asset.setDescription(description);
        asset.setUrl(url);
        asset.setId(Long.parseLong("20"));
        asset.setCategory(category);
        assetBO.createAsset(asset);
        return "/createCrypto" ;
    }
}
