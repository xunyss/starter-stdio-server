/*
* Copyright 2024 - 2024 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.springframework.ai.mcp.sample.client;

import java.util.Map;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

/**
 * With stdio transport, the MCP server is automatically started by the client. But you
 * have to build the server jar first:
 *
 * <pre>
 * ./mvnw clean install -DskipTests
 * </pre>
 */
public class ClientStdio {

	// https://github.com/spring-projects/spring-ai-examples/tree/main/model-context-protocol/weather/starter-stdio-server

	public static void main(String[] args) {

		var stdioParams = ServerParameters.builder("java")
				.args(	"-Dspring.ai.mcp.server.transport=STDIO",
						"-Dspring.main.web-application-type=none",
						"-Dlogging.pattern.console=",
						"-jar",
					"/Users/xuny/xdev/works/starter-stdio-server/target/mcp-weather-stdio-server-0.0.1-SNAPSHOT.jar")
				.build();

		var transport = new StdioClientTransport(stdioParams);
		var client = McpClient.sync(transport).build();

		client.initialize();
		System.out.println();

		// List and demonstrate tools
		ListToolsResult toolsList = client.listTools();
		System.out.println("Available Tools = " + toolsList);
		System.out.println();

		CallToolResult weatherForecastResult =
				client.callTool(
						new CallToolRequest("getWeatherForecastByLocation",
						Map.of(	"latitude", 47.6062,
								"longitude", -122.3321)
						)
		);
		System.out.println("Weather Forecast: " + weatherForecastResult);
		System.out.println();

		CallToolResult alertResult =
				client.callTool(
						new CallToolRequest("getAlerts", Map.of("state", "CA"))
				);
		System.out.println("Alert Response = " + alertResult);
		System.out.println();

		client.closeGracefully();
	}

}
