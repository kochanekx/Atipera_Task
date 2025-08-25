package pl.szlify.atipera.model.github;

public record GitHubBranchResponse(String name, Commit commit) {
    public record Commit(String sha) {}
}