package com.restmonkeys.backetdb.aq;

import com.restmonkeys.backetdb.client.BacketClient;
import com.restmonkeys.backetdb.server.Application;
import com.restmonkeys.backetdb.server.model.Backet;
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
    public void runServer() {
        application = new Application();
        client = BacketClient.client();
    }

    @BeforeMethod
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
}
