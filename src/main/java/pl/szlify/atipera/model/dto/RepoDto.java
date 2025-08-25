package pl.szlify.atipera.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RepoDto {
    private String repositoryName;
    private String ownerLogin;
    private List<BranchDto> branches;


}
