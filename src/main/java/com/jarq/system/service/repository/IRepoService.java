package com.jarq.system.service.repository;

import com.jarq.system.service.IService;

public interface IRepoService extends IService {

    String createRepository(int userId, String repositoryName);

    String changeRepositoryName(int repositoryId, String repositoryName);

    String[] getUserRepositories(int userId);

    String removeRepository(int repositoryId);
}
