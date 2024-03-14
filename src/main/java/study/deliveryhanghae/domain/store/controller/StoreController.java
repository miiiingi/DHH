package study.deliveryhanghae.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.StoreListDto;
import study.deliveryhanghae.domain.store.service.StoreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public String getMainPage(Model model){
        List<StoreListDto> storeList = storeService.getStoreList();
        model.addAttribute("stores",storeList);
        return "main";
    }

    @GetMapping("/search")
    public String getSearchStore(
            Model model,
            @RequestParam("menu") String menuName){
        List<StoreListDto> storeList = storeService.getSearchStroeList(menuName);
        model.addAttribute("stores",storeList);
        return "main";
    }

}
