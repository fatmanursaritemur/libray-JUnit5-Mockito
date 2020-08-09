package com.fatmanur.unittest.mockito;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.eq;

public class SampleMockWithRestaurantTest {

    @Test
    void addOrderToCustomer() {

        final Stock stockMock = Mockito.mock(Stock.class);
        Mockito.when(stockMock.isStockAvailable("Reuben", "Salisbury Steak")).thenReturn(true);
        Mockito.when(stockMock.isStockAvailable("Carpaccio")).thenReturn(false);

        Customer customer = new Customer(stockMock);
        assertTrue(customer.addOrderingMeal(new Meal(List.of("Reuben", "Salisbury Steak"))));
        assertFalse(customer.addOrderingMeal(new Meal(List.of("Carpaccio"))));
        Mockito.verify(stockMock, Mockito.times(1)).isStockAvailable(eq("Reuben"), eq("Salisbury Steak"));
        Mockito.verify(stockMock, Mockito.times(1)).isStockAvailable(eq("Carpaccio"));
    }

    private class Customer {

        final Stock stock;

        public Customer(Stock stock) {
            this.stock = stock;
        }

        boolean addOrderingMeal(Meal meal) {
            if (!stock.isStockAvailable(meal.items.toArray(new String[]{}))) {
                return false;
            }
            return true;
        }
    }

    private class Meal {

        List<String> items = new ArrayList<>();

        Meal(List<String> items) {
            this.items.addAll(items);
        }

    }

    private interface Stock {

        boolean isStockAvailable(String... items);
    }

}