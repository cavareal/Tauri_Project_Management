package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.repository.FlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlagService {

    private final FlagRepository flagRepository;

    public Flag addFlag(Flag flag) {
        return flagRepository.save(flag);
    }
}
