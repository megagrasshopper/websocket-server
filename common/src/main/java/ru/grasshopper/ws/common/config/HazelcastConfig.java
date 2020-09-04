package ru.grasshopper.ws.common.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("hazelcast")
@Slf4j
@Getter
@Setter
public class HazelcastConfig {
    private String login;
    private String password;
    private String hosts;
    private Integer connectionAttemptLimit;
    private Integer userDataTtlSeconds;
    private Integer pongTtlSeconds;

    @Bean
    @Profile({"local", "test"})
    public HazelcastInstance getLocalHazelcastInstance() {
        log.info("=== Starting local Hazelcast instance ===");

        Config cfg = new Config();
        NetworkConfig network = cfg.getNetworkConfig();

        JoinConfig join = network.getJoin();
        join.getTcpIpConfig().setEnabled(false);
        join.getMulticastConfig().setEnabled(true);

        initCaches(cfg);

        return Hazelcast.newHazelcastInstance(cfg);
    }

    @Bean
    @Profile({"dev", "prod"})
    public HazelcastInstance getHazelcastInstance() {
        log.info("=== Connecting to Hazelcast on {} with login {} ===", hosts, login);

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setGroupConfig(new GroupConfig(login, password));
        clientConfig.getNetworkConfig()
                .addAddress(hosts.split(","))
                .setConnectionAttemptLimit(connectionAttemptLimit);

        HazelcastInstance hzInstance = HazelcastClient.newHazelcastClient(clientConfig);
        initCaches(hzInstance.getConfig());

        return hzInstance;
    }

    private void initCaches(Config cfg) {

        cfg.addMapConfig(new MapConfig()
                .setTimeToLiveSeconds(userDataTtlSeconds)
                .setName(MapNames.SLEEPING)
                .addMapIndexConfig(new MapIndexConfig("timestamp", true)));

        cfg.addMapConfig(new MapConfig()
                .setTimeToLiveSeconds(pongTtlSeconds)
                .setName(MapNames.PONG));
    }
}