package com.itwray.iw.generator;

/**
 * 代码生成类工具
 *
 * @author wray
 * @since 2025/4/21
 */
public class CodeGeneratorUtil {

    public static void main(String[] args) {
        GlobalConfig config = GlobalConfig.builder()
                .dbUrl("jdbc:mysql://localhost:3306/iw_mixes?serverTimezone=Asia/Shanghai")
                .dbUsername("iw_root")
                .dbPassword("iw@2024")
                .author("wray")
                .outputDir("/Users/wangfarui/workspaces/wfr/iw-mixes/iw-packaging-parent/iw-bookkeeping")
                .parentPackage("com.itwray.iw.bookkeeping")
                .enabledWebModule(true)
                .enableFileOverride(false)
                .build();

        CodeGenerator.generate(config, "bookkeeping_membership_subscription");
    }
}
