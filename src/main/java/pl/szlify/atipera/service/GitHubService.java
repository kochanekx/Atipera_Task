package pl.szlify.atipera.service;

import org.springframework.stereotype.Service;
import pl.szlify.atipera.model.dto.BranchDto;
import pl.szlify.atipera.model.dto.RepoDto;
import pl.szlify.atipera.model.github.GitHubBranchResponse;
import pl.szlify.atipera.model.github.GitHubClient;
import pl.szlify.atipera.model.github.GitHubRepoResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private final GitHubClient gitHubClient;

    public GitHubService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public List<RepoDto> findNonForkReposWithBranches(String username) {
        List<GitHubRepoResponse> repos = gitHubClient.listUserRepos(username);

        return repos.stream()
                .filter(r -> !r.fork())
                .map(r -> {
                    List<GitHubBranchResponse> branches = gitHubClient.listRepoBranches(r.owner().login(), r.name());
                    List<BranchDto> branchDtos = branches.stream()
                            .map(b -> new BranchDto(b.name(), b.commit().sha()))
                            .collect(Collectors.toList());

                    return new RepoDto(r.name(), r.owner().login(), branchDtos);
                })
                .collect(Collectors.toList());
    }
}
