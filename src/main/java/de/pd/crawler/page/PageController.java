package de.pd.crawler.page;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pages")
public class PageController {

    private final PageRepository pageRepository;

    public PageController(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @PostMapping
    public PageEntity createPage(@RequestBody PageEntity pageEntity) {
        return pageRepository.save(pageEntity);
    }

    @DeleteMapping("/{id}")
    public void deletePageById(@PathVariable("id") Long id){
        pageRepository.deleteById(id);
    }
}
