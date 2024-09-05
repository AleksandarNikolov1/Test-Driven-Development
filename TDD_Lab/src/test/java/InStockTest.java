import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InStockTest {

    private ProductStock inStock;
    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private Product product5;
    private Product product6;

    @Before
    public void setUp(){
        inStock = new InStock();
        product1 = new Product("label_test_1", 100, 1);
        product2 = new Product("label_test_2", 200, 2);
        product3 = new Product("label_test_3", 300, 3);
        product4 = new Product("label_test_4", 355.25, 4);
        product5 = new Product("label_test_5", 155.45, 5);
        product6 = new Product("label_test_6", 299.99, 6);
    }

    private void addProducts(){
        inStock.add(product1);
        inStock.add(product2);
        inStock.add(product3);
        inStock.add(product4);
        inStock.add(product5);
        inStock.add(product6);
    }

    @Test
    public void testAddProduct(){
        Product product = new Product("label_test_1", 100, 1);
        inStock.add(product);
        assertTrue(inStock.contains(product));
    }

    @Test
    public void testGetCountReturnsCorrectValue(){
        addProducts();
        assertEquals(Integer.valueOf(6), inStock.getCount());
    }

    @Test
    public void testGetCountReturnsZeroForEmptyCollection(){
        InStock emptyStock = new InStock();
        assertEquals(Integer.valueOf(0), emptyStock.getCount());
    }

    @Test
    public void testFindReturnsProductForValidIndex(){
        addProducts();
        Product foundProduct = inStock.find(1);

        assertEquals(product2, foundProduct);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindFailsForInvalidIndex(){
        inStock.find(6);
    }

    @Test
    public void testChangeQuantityByProductName(){
        addProducts();
        inStock.changeQuantity("label_test_1", 500);
        assertEquals(500, product1.getQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeQuantityFailsForInvalidProductName(){
        inStock.changeQuantity("invalid_label", 500);
    }

    @Test
    public void testFindByLabelForExistingProduct(){
        addProducts();
        Product foundProduct = inStock.findByLabel("label_test_3");
        assertEquals(product3, foundProduct);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByLabelFailsForNoExistingProduct(){
        addProducts();
        Product noExistingProduct = inStock.findByLabel("I don't exist");
    }

    @Test
    public void testFindFirstByAlphabeticalOrderForGivenCount(){
        product1 = new Product("C", 200, 1);
        product2 = new Product("A", 300, 3);
        product3 = new Product("B", 100, 2);
        Product product4 = new Product("D", 400, 4);

        inStock.add(product1);
        inStock.add(product2);
        inStock.add(product3);
        inStock.add(product4);

        Iterable<Product> iterableProducts = inStock.findFirstByAlphabeticalOrder(3);

        List<Product> products = new ArrayList<>();

        iterableProducts.forEach(products::add);

        assertNotNull(products);
        assertEquals(3, products.size());
        assertEquals("A", products.get(0).getLabel());
        assertEquals("B", products.get(1).getLabel());
        assertEquals("C", products.get(2).getLabel());
    }

    @Test
    public void testFindFirstByAlphabeticalOrderReturnsEmptyCollectionForInvalidCount(){
        addProducts();
        Iterable<Product> iterableProducts = inStock.findFirstByAlphabeticalOrder(8);
        List<Product> products = new ArrayList<>();
        iterableProducts.forEach(products::add);
        assertNotNull(products);
        assertEquals(0, products.size());
    }

    @Test
    public void testFindAllInPriceRangeReturnsProductsCollection(){
        addProducts();
        Iterable<Product> iterableProducts = inStock.findAllInRange(155.45, 355.25);
        List<Product> products = new ArrayList<>();
        iterableProducts.forEach(products::add);
        assertEquals(4, products.size());
        assertEquals(355.25, products.get(0).getPrice(), 0.01);
        assertEquals(300, products.get(1).getPrice(), 0.01);
        assertEquals(299.99, products.get(2).getPrice(), 0.01);
        assertEquals(200, products.get(3).getPrice(), 0.01);
    }

    @Test
    public void testFindAllInPriceRangeReturnsEmptyCollectionForInvalidRange(){
        addProducts();
        Iterable<Product> iterableProducts = inStock.findAllInRange(355.25, 100);
        List<Product> products = new ArrayList<>();
        iterableProducts.forEach(products::add);
        assertNotNull(products);
        assertEquals(0, products.size());
    }

    @Test
    public void testFindAllByPriceReturnsProductsCollection(){
        addProducts();

        Product product7 =  new Product("label_test_7", 299.99, 7);
        inStock.add(product7);

        Iterable<Product> iterableProducts = inStock.findAllByPrice(299.99);
        List<Product> products = new ArrayList<>();
        iterableProducts.forEach(products::add);
        assertEquals(2, products.size());
    }

    @Test
    public void testFindAllByPriceReturnsEmptyCollectionForInvalidPrice(){
        addProducts();

        Iterable<Product> iterableProducts = inStock.findAllByPrice(444.44);
        List<Product> products = new ArrayList<>();
        iterableProducts.forEach(products::add);
        assertEquals(0, products.size());
    }

    @Test
    public void testFindFirstMostExpensiveProductsByGivenCount(){
        addProducts();
        Iterable<Product> iterableProducts = inStock.findFirstMostExpensiveProducts(4);
        List<Product> products = new ArrayList<>();
        iterableProducts.forEach(products::add);
        assertEquals(4, products.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstMostExpensiveProductsFailsForInvalidCount(){
        addProducts();
        Iterable<Product> iterableProducts = inStock.findFirstMostExpensiveProducts(7);
    }

    @Test
    public void testFindAllByQuantityByGivenCount(){
        addProducts();
        Product product7 =  new Product("label_test_7", 700, 6);
        inStock.add(product7);
        Iterable<Product> iterableProducts = inStock.findAllByQuantity(6);
        List<Product> products = new ArrayList<>();
        iterableProducts.forEach(products::add);
        assertEquals(2, products.size());
    }

    @Test
    public void testFindAllByQuantityReturnsEmptyCollectionForInvalidCount(){
        addProducts();
        Iterable<Product> iterableProducts = inStock.findAllByQuantity(13);
        List<Product> products = new ArrayList<>();
        iterableProducts.forEach(products::add);
        assertEquals(0, products.size());
    }

    @Test
    public void testGetIterableReturnsAllProductsInStock(){
        addProducts();
        List<Product> products = new ArrayList<>();

        for (Product product : inStock) {
            products.add(product);
        }
        assertEquals(6, products.size());
    }
}
