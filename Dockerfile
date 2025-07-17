FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/expenssManeger-0.0.1-SNAPSHOT.jar moneymaneger-v1.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "moneymaneger-v1.0.jar"]
