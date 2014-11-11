package com.restmonkeys.backetdb.aq;

import com.restmonkeys.backetdb.client.BacketClient;
import com.restmonkeys.backetdb.server.Application;
import com.restmonkeys.backetdb.server.model.Backet;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
}
