package org.blackchain.model.portfolio;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssetPerformance {

     private String assetName;
     private List<PairPerformance> performanceList;
}


