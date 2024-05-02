package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Flag;
import fr.eseo.tauri.repository.FlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlagService {

    private final FlagRepository flagRepository;

    public Flag addFlag(Flag flag) {
        return flagRepository.save(flag);
    }

    public List<Flag> getFlagsByAuthorAndDescription(Integer authorId , String description) {
        return flagRepository.findByAuthorIdAndDescription(authorId, description);
    }
}
