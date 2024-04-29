package fr.eseo.tauri.model.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, int id) {
        super("The " + resource + " with id " + id + " has not been found.");
    }
}
