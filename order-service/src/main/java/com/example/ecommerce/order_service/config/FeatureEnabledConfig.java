package com.example.ecommerce.order_service.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
@Data
public class FeatureEnabledConfig{

    @Value("${feature.tracking.enabled}")
    private boolean isFeatureTrackingEnabled;
}
