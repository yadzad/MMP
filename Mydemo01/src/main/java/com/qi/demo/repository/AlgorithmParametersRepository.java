package com.qi.demo.repository;


import com.qi.demo.dataobject.AlgorithmParameters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlgorithmParametersRepository extends JpaRepository<AlgorithmParameters, Integer> {

    List<AlgorithmParameters> findByProjectId(String projectId);

    AlgorithmParameters findByAlgorithmIntegerId(Integer aId);

    AlgorithmParameters findByAlgorithmId(String algorithmId);


}
