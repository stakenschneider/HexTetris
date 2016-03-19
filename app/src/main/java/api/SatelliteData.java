package api;

import backport.Optional;


public interface SatelliteData {

    <T> void addCustomData(String key, T data);

    <T> Optional<T> getCustomData(String key);

}