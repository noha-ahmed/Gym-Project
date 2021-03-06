package com.example.gymserver;

import com.example.gymserver.controllers.ShopController;
import com.example.gymserver.dto.CartDTO;
import com.example.gymserver.dto.OrderItemDTO;
import com.example.gymserver.dto.ProductDTO;
import com.example.gymserver.dto.UserDTO;
import com.example.gymserver.repositories.ShopRepository;
import com.example.gymserver.repositories.UserRepository;
import com.example.gymserver.services.ShopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ShopServiceTest {

    @Autowired
    private ShopController shopController;
    @Autowired
    private UserRepository userRepository ;
    @Autowired
    private ShopRepository shopRepository ;

    private static UserDTO registeredUser;

    @Test
    public void showProductsClothesCategory(){
        List<ProductDTO> products = this.shopController.showProducts("Clothes");
        assertEquals(9,products.size());
    }

    @Test
    public void showProductsEquipmentCategory(){
        List<ProductDTO> products = this.shopController.showProducts("Equipments");
        assertEquals(9,products.size());
    }

    @Test
    public void showProductsSuppliesCategory(){
        List<ProductDTO> products = this.shopController.showProducts("Supplies");
        assertEquals(9,products.size());
    }

    @Test
    public void validConfirmOrder(){
        int inStockProd1Before = this.shopRepository.findById(25L).orElse(null).getNoInStock();
        int inStockProd2Before = this.shopRepository.findById(2L).orElse(null).getNoInStock();
        OrderItemDTO orderItemDTO1 = new OrderItemDTO("True Stones Protein Powder", 25, 300,1,30,300);
        OrderItemDTO orderItemDTO2 = new OrderItemDTO("Leggings medium", 2, 300,1,100,300);
        List<OrderItemDTO> orderItems = new LinkedList<>();
        orderItems.add(orderItemDTO1);
        orderItems.add(orderItemDTO2);
        CartDTO cart = new CartDTO(0, orderItems);
        String actual = this.shopController.confirmOrder("mariam", cart);
        // making sure it decremented the in stock quantity
        int inStockProd1After = this.shopRepository.findById(25L).orElse(null).getNoInStock();
        int inStockProd2After = this.shopRepository.findById(2L).orElse(null).getNoInStock();

        assertEquals("Order confirmed successfully!", actual);
        assertEquals(inStockProd1Before - 1, inStockProd1After);
        assertEquals(inStockProd2Before - 1, inStockProd2After);
    }

    @Test
    public void invalidConfirmOrderOutOfStock(){
        int inStockProdBefore = this.shopRepository.findById(3L).orElse(null).getNoInStock();
        OrderItemDTO orderItemDTO1 = new OrderItemDTO("Pink Hoddie large", 3, 400,1,200,400);
        List<OrderItemDTO> orderItems = new LinkedList<>();
        orderItems.add(orderItemDTO1);
        CartDTO cart = new CartDTO(0, orderItems);
        String actual = this.shopController.confirmOrder("mariam", cart);
        // making sure it decremented the in stock quantity
        int inStockProdAfter = this.shopRepository.findById(3L).orElse(null).getNoInStock();

        assertEquals( "Item Pink Hoddie large is out of stock.", actual);
        assertEquals(inStockProdBefore , inStockProdAfter);
    }

    @Test
    public void invalidConfirmOrderQuantityNotEnough(){
        int inStockProd1Before = this.shopRepository.findById(17L).orElse(null).getNoInStock();
        int inStockProd2Before = this.shopRepository.findById(4L).orElse(null).getNoInStock();
        OrderItemDTO orderItemDTO1 = new OrderItemDTO("Rose Water Bottle", 17, 200,50,30,200);
        OrderItemDTO orderItemDTO2 = new OrderItemDTO("Pink Hoddie xlarge", 4, 400,1,200,400);
        List<OrderItemDTO> orderItems = new LinkedList<>();
        orderItems.add(orderItemDTO1);
        orderItems.add(orderItemDTO2);
        CartDTO cart = new CartDTO(0, orderItems);
        String actual = this.shopController.confirmOrder("mariam", cart);
        // making sure it decremented the in stock quantity
        int inStockProd1After = this.shopRepository.findById(17L).orElse(null).getNoInStock();
        int inStockProd2After = this.shopRepository.findById(4L).orElse(null).getNoInStock();

        assertEquals( "Item Rose Water Bottle has only 30 in stock.", actual);
        assertEquals(inStockProd1Before , inStockProd1After);
        assertEquals(inStockProd2Before , inStockProd2After);
    }

    @Test
    public void unauthenticatedUserConfirmOrder(){
        List<OrderItemDTO> orderItems = new LinkedList<>();
        orderItems.add(new OrderItemDTO("Leggings medium", 2, 300f, 1, 100, 300f));
        CartDTO cartDTO = new CartDTO(2, orderItems);
        String actual = this.shopController.confirmOrder("mariam", cartDTO);
        assertEquals("Invalid User!!", actual);
    }
}
