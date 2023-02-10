package com.energybox.backendcodingchallenge.dto;

import com.energybox.backendcodingchallenge.domain.Gateway;

import java.util.Objects;

public class GatewayResultDto {

    private final Gateway gateway;

    public GatewayResultDto(Gateway gateway) {
        this.gateway = gateway;
    }

    public Gateway getGateway() {
        return gateway;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GatewayResultDto that = (GatewayResultDto) o;
        return Objects.equals(gateway, that.gateway);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gateway);
    }
}
