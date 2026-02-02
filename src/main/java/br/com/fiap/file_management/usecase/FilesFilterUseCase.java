package br.com.fiap.file_management.usecase;

import br.com.fiap.file_management.converter.FileConverter;
import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.entity.FileEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilesFilterUseCase {

    public static List<File> findFiltered(String email, String status, EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileEntity> query = cb.createQuery(FileEntity.class);
        Root<FileEntity> root = query.from(FileEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (email != null && !email.isEmpty()) {
            predicates.add(cb.equal(root.join("user").get("email"), email));
        }

        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        query.where(predicates.toArray(new Predicate[0]));

        List<FileEntity> results = entityManager.createQuery(query).getResultList();

        return results.stream()
                .map(FileConverter::toDomain)
                .collect(Collectors.toList());
    }
}
