package main.java.com.adrian.gameconcepthub.domain.port.out;

import main.java.com.adrian.gameconcepthub.domain.model.Category;
import java.util.List;

public interface CategoryRepository {

    Category save(Category category);

    List<Category> findAll();
}
