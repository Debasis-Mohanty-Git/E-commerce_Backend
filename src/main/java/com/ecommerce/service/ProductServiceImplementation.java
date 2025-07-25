package com.ecommerce.service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.request.CreateProductRequest;

@Service
public class ProductServiceImplementation implements ProductService{
	
   @Autowired	
   private ProductRepository productRepository;
   @Autowired
   private UserService userService;
   @Autowired
   private CategoryRepository categoryRepository;

   @Override
   public Product createProduct(CreateProductRequest req) {
       // First Level Category
       Category topLevel = categoryRepository.findByName(req.getTopLevelCategory());
       if (topLevel == null) {
           Category topLevelCategory = new Category();
           topLevelCategory.setName(req.getTopLevelCategory());
           topLevelCategory.setLevel(1);
           topLevel = categoryRepository.save(topLevelCategory);
       }

       // Second Level Category
       Category secondLevel = categoryRepository.findByNameAndParentCategory(req.getSecondLevelCategory(), topLevel);
       if (secondLevel == null) {
           Category secondLevelCategory = new Category();
           secondLevelCategory.setName(req.getSecondLevelCategory());
           secondLevelCategory.setParentCategory(topLevel);
           secondLevelCategory.setLevel(2);
           secondLevel = categoryRepository.save(secondLevelCategory);
       }

    // Third Level Category (mens_kurta under "Clothing" under "Men")
       String thirdLevelCategoryName = req.getThirdLevelCategory();

       if (thirdLevelCategoryName == null || thirdLevelCategoryName.trim().isEmpty()) {
           throw new IllegalArgumentException("Third level category name must not be null or empty");
       }

       Category thirdLevel = categoryRepository.findByNameAndParentCategory(thirdLevelCategoryName, secondLevel);

       if (thirdLevel == null) {
           thirdLevel = new Category();
           thirdLevel.setName(thirdLevelCategoryName);
           thirdLevel.setParentCategory(secondLevel);
           thirdLevel.setLevel(3);

           thirdLevel = categoryRepository.save(thirdLevel); // ✅ now name is guaranteed non-null
       }


       // Create Product
       Product product = new Product();
       product.setTitle(req.getTitle());
       product.setColor(req.getColor());
       product.setDescription(req.getDescription());
       product.setDiscountedPrice(req.getDiscountedPrice());
       product.setDiscountPresent(req.getDiscountPresent());
       product.setImageUrl(req.getImageUrl());
       product.setBrand(req.getBrand());
       product.setPrice(req.getPrice());
       product.setSizes(req.getSize());
       product.setQuantity(req.getQuantity());
       product.setCategory(thirdLevel); // final category
       product.setCreatedAt(LocalDateTime.now());

       return productRepository.save(product);
   }


	@Override
	public String deleteProduct(Long productId) throws ProductException {
        Product product=findProductById(productId);
        product.getSizes().clear();
        productRepository.delete(product);
        
		return "Product deleted Successfully";
	}

	@Override
	public Product updateProduct(Long productId, Product req) throws ProductException {
		Product product=findProductById(productId);
		
		if(req.getQuantity()!=0)
		{
			product.setQuantity(req.getQuantity());
		}
		
		return productRepository.save(product);
	}

	@Override
	public Product findProductById(Long id) throws ProductException {
		Optional<Product> opt=productRepository.findById(id);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new ProductException("Product not found with this Id"+id);
	}

	@Override
	public List<Product> findProductByCategory(String category) throws ProductException {
		
		return null;
	}

	@Override
	public Page<Product> getAllProduct(String category, List<String> color, List<String> size,
	                                   Integer minPrice, Integer maxPrice, Integer minDiscount,
	                                   String sort, String stock, Integer pageNumber, Integer pageSize) {

	    Pageable pageable = PageRequest.of(pageNumber, pageSize);

	    // Step 1: DB filter for category, price, discount
	    List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount);

	    // Step 2: Filter by color
	    if (color != null && !color.isEmpty()) {
	        products = products.stream()
	            .filter(p -> color.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
	            .collect(Collectors.toList());
	    }

	    // Step 3: Filter by size
	    if (size != null && !size.isEmpty()) {
	        products = products.stream()
	            .filter(p -> p.getSizes().stream().anyMatch(s -> size.contains(s.getName())))
	            .collect(Collectors.toList());
	    }

	    // Step 4: Filter by stock
	    if (stock != null) {
	        if (stock.equals("in_stock")) {
	            products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
	        } else if (stock.equals("out_of_stock")) {
	            products = products.stream().filter(p -> p.getQuantity() <= 0).collect(Collectors.toList());
	        }
	    }

	    // Step 5: Sorting
	    if (sort != null) {
	        if (sort.equals("Price_low")) {
	            products.sort(Comparator.comparingInt(Product::getDiscountedPrice));
	        } else if (sort.equals("Price_high")) {
	            products.sort(Comparator.comparingInt(Product::getDiscountedPrice).reversed());
	        }
	    }

	    // Step 6: Pagination
	    int startIndex = (int) pageable.getOffset();
	    int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());
	    List<Product> pageContent = products.subList(startIndex, endIndex);

	    return new PageImpl<>(pageContent, pageable, products.size());
	}


}
