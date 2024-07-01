<#-- pojo-template.ftl -->
package ${packageName};

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ${className} {

<#list attributes as attribute>
    private ${attribute.type} ${attribute.name};
</#list>

}
