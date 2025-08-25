package pl.szlify.atipera.model.github;

public record GitHubRepoResponse(String name, boolean fork, Owner owner) {
    public record Owner(String login) {}
}
