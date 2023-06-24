package com.github.bernardflo.stc;

public class App {

    public static void main(String[] args) {
        try {
            var apiInfoList = new SwaggerReader().read();
            apiInfoList.forEach(apiInfo -> {
                System.out.println("URL: " + apiInfo.method() + " " + apiInfo.path());
                System.out.println("Description: " + apiInfo.description());
                System.out.println("Parameters:");
                apiInfo.parameters().forEach(parameter -> {
                    System.out.println("   " + parameter.name() + " : " + parameter.description());
                });
                System.out.println("\n\n");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
