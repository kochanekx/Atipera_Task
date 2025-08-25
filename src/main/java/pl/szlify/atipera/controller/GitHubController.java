package pl.szlify.atipera.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.szlify.atipera.model.dto.RepoDto;
import pl.szlify.atipera.service.GitHubService;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/{username}/repos")
    @ResponseStatus(HttpStatus.OK)
    public List<RepoDto> getRepos(@PathVariable String username) {
        return gitHubService.findNonForkReposWithBranches(username);
    }
}