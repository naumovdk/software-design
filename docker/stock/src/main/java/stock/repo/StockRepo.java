package stock.repo;

import org.springframework.data.repository.CrudRepository;
import stock.model.Stock;

public interface StockRepo extends CrudRepository<Stock, String> {
}
