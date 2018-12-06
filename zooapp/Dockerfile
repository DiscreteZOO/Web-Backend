# Start from the sbt builder image
FROM kwarc/sbt-builder as builder

# Add dependencies
ADD project/ /zoo/project
ADD src/ /zoo/src
ADD build.sbt /zoo/build.sbt

# Build the jar file
WORKDIR /zoo/
RUN sbt assembly

# Create new image with java and zoo.jar
FROM openjdk:jre-alpine
WORKDIR /zoo/
COPY --from=builder /zoo/dist/zoo.jar /zoo/zoo.jar

# Set database credentials
ENV ZOODB_JDBC=jdbc:postgresql://localhost:5432/discretezoo2
ENV ZOODB_USER=discretezoo
ENV ZOODB_PASS=D!screteZ00

# Expose port 8080
EXPOSE 8080
CMD [ "java", "-jar", "zoo.jar", "8080", "0.0.0.0"]