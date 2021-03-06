package com.restmonkeys.backetdb.aq;

import com.restmonkeys.backetdb.client.BacketClient;
import com.restmonkeys.backetdb.server.Application;
import com.restmonkeys.backetdb.server.model.Backet;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class APITest {

    private Application application;
    private BacketClient client;

    @BeforeSuite
    @BeforeGroups(groups = "load")
    public void runServer() {
        application = new Application();
        client = BacketClient.client();
    }

    @BeforeMethod
    @BeforeGroups(groups = "load")
    public void clearServer() {
        client.getAllBackets().parallelStream().map(Backet::getId).map(Optional::get).forEach(client::deleteBacket);
    }

    @Test
    public void getEmptyListOfBackets() {
        // setup

        // act
        List<Backet> allBackets = client.getAllBackets();

        // versify
        assertThat(allBackets.size(), is(0));
    }

    @Test
    public void createOneBacket() {
        // setup

        // act
        Backet result = client.createBacket();

        // verify
        assertNotNull(result);
        List<Backet> allBackets = client.getAllBackets();
        assertThat(allBackets.size(), is(1));
        assertThat(allBackets.get(0).getId().get(), is(result.getId().get()));
    }

    @Test
    public void addItemToBacket() {
        // setup
        Backet backet = client.createBacket();
        String item = "hello";

        // act
        Backet result = client.addItem(backet, item);

        // verify
        assertThat(result.getItems().size(), is(1));
        assertThat(result.getItems().get(0), is(item));
    }

    @Test
    public void massAddItemsToBacket() {
        // setup
        Backet backet = client.createBacket();
        String item = "hello";
        int count = 1000;

        // act
        IntStream.rangeClosed(1, count).parallel().forEach(i -> client.addItem(backet, item + i));

        Backet result = client.getBacket(backet.getId().get());

        // verify
        assertThat(result.getItems().size(), is(count));
        assertTrue(result.getItems().contains(item + 1));
    }

    @Test
    public void massBacketCreation() throws Exception {
        // setup
        int count = 1000;
        // act
        IntStream.rangeClosed(1, count).parallel().forEach(i -> client.createBacket());

        // verify
        List<Backet> allBackets = client.getAllBackets();
        assertThat(allBackets.size(), is(count));
    }

    @Test
    public void sumNumbers() {
        // setup
        IntStream range = IntStream.range(1, 5);
        double sum = range.sum();

        Backet backet = client.createBacket("{" +
                "  \"aggregates\": [ {" +
                "    \"name\": \"sum\"," +
                "    \"code\": \"var sum = function(list) { var result = 0;for(var i = 0; i< list.length; i++)result+=list[i]; return result;}\"}]" +
                "}");

        // act
        IntStream.range(1, 5).forEach(i -> client.addItem(backet, String.valueOf(i)));
        Backet result = client.getBacket(backet.getId().get());

        // verify
        assertThat(result.getResults().size(), is(1));
        assertThat(result.getResults().get("sum"), is(sum));
    }

    @Test(groups = "load")
    public void sumLotOfNumbers_withReusingPreviousResults() {
        // setup
        int count = 1000;
        IntStream range = IntStream.range(1, count);
        double sum = range.sum();

        Backet backet = client.createBacket("{" +
                "  \"aggregates\": [ {" +
                "    \"name\": \"sum\"," +
                "    \"code\": \"var sum = function(list, prevRes, newItem) { return +prevRes + +newItem;}\"}]" +
                "}");

        // act
        IntStream.range(1, count).parallel().forEach(i -> client.addItem(backet, String.valueOf(i)));
        Backet result = client.getBacket(backet.getId().get());

        // verify
        assertThat(result.getResults().size(), is(1));
        assertThat(result.getResults().get("sum"), is(sum));
    }

    @Test(groups = "load")
    public void sumLotOfNumbers_withoutReusingPreviousResults() {
        // setup
        int count = 1000;
        IntStream range = IntStream.range(1, count);
        double sum = range.sum();

        Backet backet = client.createBacket("{" +
                "  \"aggregates\": [ {" +
                "    \"name\": \"sum\"," +
                "    \"code\": \"var sum = function(list) { var result = 0;for(var i = 0; i< list.length; i++)result+=list[i]; return result;}\"}]" +
                "}");

        // act
        IntStream.range(1, count).parallel().forEach(i -> client.addItem(backet, String.valueOf(i)));
        Backet result = client.getBacket(backet.getId().get());

        // verify
        assertThat(result.getResults().size(), is(1));
        assertThat(result.getResults().get("sum"), is(sum));
    }

    @Test
    public void findMaximum() {
        // setup
        IntStream range = IntStream.range(1, 5);
        int max = range.max().getAsInt();
        Backet backet = client.createBacket("{" +
                "  \"aggregates\": [ {" +
                "    \"name\": \"max\"," +
                "    \"code\": \"var max = function(list) { return Math.max.apply(null, list)}\"}]" +
                "}");

        // act
        IntStream.range(1, 5).forEach(i -> client.addItem(backet, String.valueOf(i)));
        Backet result = client.getBacket(backet.getId().get());

        // verify
        assertThat(result.getResults().size(), is(1));
        assertThat(((Double) result.getResults().get("max")).intValue(), is(max));
    }
}
