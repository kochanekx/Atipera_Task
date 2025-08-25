package pl.szlify.atipera.model.github;


import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.szlify.atipera.exception.GitHubUserNotFoundException;

import java.util.List;

@Component
public class GitHubClient {

    private final RestTemplate restTemplate;

    public GitHubClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<GitHubRepoResponse> listUserRepos(String username) {
        String url = "https://api.github.com/users/" + username + "/repos?per_page=100";
        try {
            GitHubRepoResponse[] repos = restTemplate.getForObject(url, GitHubRepoResponse[].class);
            return List.of(repos);
        } catch (HttpClientErrorException.NotFound e) {
            throw new GitHubUserNotFoundException("GitHub user " + username + " not found");
        }
    }

    public List<GitHubBranchResponse> listRepoBranches(String owner, String repo) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/branches?per_page=100";
        try {
            GitHubBranchResponse[] branches = restTemplate.getForObject(url, GitHubBranchResponse[].class);
            return List.of(branches);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Failed to fetch branches for " + repo);
        }
    }
}
