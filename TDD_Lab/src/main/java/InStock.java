import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InStock implements ProductStock {

    private List<Product> products;

    public InStock() {
        products = new ArrayList<>();
    }

    @Override
    public Integer getCount() {
        return products.size();
    }

    @Override
    public boolean contains(Product product) {
        return products.contains(product);
    }

    @Override
    public void add(Product product) {
        products.add(product);
    }

    @Override
    public void changeQuantity(String label, int quantity) {
        Product searchedProduct = products.stream().filter(p -> p.getLabel().equals(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        searchedProduct.setQuantity(quantity);
    }

    @Override
    public Product find(int index) {
        if (index < 0 || index > products.size() - 1)
            throw new IndexOutOfBoundsException();

        return products.get(index);
    }

    @Override
    public Product findByLabel(String label) {
        return products.stream().filter(p -> p.getLabel().equals(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Iterable<Product> findFirstByAlphabeticalOrder(int count) {
        if (count > products.size())
            return new ArrayList<>();

        List<Product> orderedProducts = new ArrayList<>();
        products.stream().sorted((p1, p2) -> p1.getLabel().compareTo(p2.getLabel())).limit(count)
                .forEach(orderedProducts::add);

        return orderedProducts;
    }

    @Override
    public Iterable<Product> findAllInRange(double lo, double hi) {
        List<Product> orderedProducts = new ArrayList<>();
        products.stream().filter(p -> p.getPrice() > lo && p.getPrice() <= hi)
                .sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
                .forEach(orderedProducts::add);

        return orderedProducts;
    }

    @Override
    public Iterable<Product> findAllByPrice(double price) {
        List<Product> orderedProducts = new ArrayList<>();
        products.stream().filter(p -> p.getPrice() > price)
                .forEach(orderedProducts::add);

        return orderedProducts;
    }

    @Override
    public Iterable<Product> findFirstMostExpensiveProducts(int count) {
        if (count > products.size())
            throw new IllegalArgumentException();

        List<Product> orderedProducts = new ArrayList<>();

        products.stream().sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
                .limit(count)
                .forEach(orderedProducts::add);

        return orderedProducts;
    }

    @Override
    public Iterable<Product> findAllByQuantity(int quantity) {
        List<Product> orderedProducts = new ArrayList<>();

        products.stream().filter(p -> p.getQuantity() == quantity)
                .forEach(orderedProducts::add);

        return orderedProducts;
    }

    @Override
    public Iterator<Product> iterator() {

        return new Iterator<Product>() {

            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < products.size();
            }

            @Override
            public Product next() {
                return products.get(index++);
            }
        };
    }
}
