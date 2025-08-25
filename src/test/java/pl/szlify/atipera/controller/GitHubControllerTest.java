package pl.szlify.atipera.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockGitHub;

    @BeforeEach
    void setUp() {
        mockGitHub = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldReturnNonForkReposWithBranches_happyPath() throws Exception {
        // Given
        String username = "john";

        String reposJson = """
            [
              {"name":"repo-a","fork":false,"owner":{"login":"john"}},
              {"name":"repo-b-fork","fork":true,"owner":{"login":"john"}}
            ]
            """;

        String branchesJson = """
            [
              {"name":"main","commit":{"sha":"aaa111"}},
              {"name":"dev","commit":{"sha":"bbb222"}}
            ]
            """;

        // When
        mockGitHub.expect(requestTo("https://api.github.com/users/" + username + "/repos?per_page=100"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(reposJson, MediaType.APPLICATION_JSON));

        mockGitHub.expect(requestTo("https://api.github.com/repos/john/repo-a/branches?per_page=100"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(branchesJson, MediaType.APPLICATION_JSON));

        // Then
        String expectedResponse = """
            [
              {
                "repositoryName":"repo-a",
                "ownerLogin":"john",
                "branches":[
                  {"name":"main","lastCommitSha":"aaa111"},
                  {"name":"dev","lastCommitSha":"bbb222"}
                ]
              }
            ]
            """;

        ResponseEntity<String> response =
                testRestTemplate.getForEntity("http://localhost:" + port + "/api/github/" + username + "/repos", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals(expectedResponse, response.getBody(), JSONCompareMode.STRICT);
        mockGitHub.verify();
    }
}
