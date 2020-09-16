package org.gatekeeper.server.vertx;

import io.vertx.core.Future;
import org.gatekeeper.server.vertx.utils.DeploymentUtils;

import java.util.List;

public class CompositeDeployer implements Deployer {
    private List<Deployer> deployers;

    public CompositeDeployer(List<Deployer> deployers) {
        this.deployers = deployers;
    }

    @Override
    public Future<Void> deploy() {
        return DeploymentUtils.sequential(deployers);
    }

}
