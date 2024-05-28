import { describe, it, expect, vi } from "vitest"
import { getTeams } from "./team.service"
import * as apiUtils from "@/utils/api"
import { Cookies } from "@/utils/cookie"
import { fakeTeam } from "@/factories/team.factory"
import { fakeResponse200, fakeResponse500 } from "@/factories/response"
import { createArray } from "@/utils/array"

global.fetch = vi.fn()
vi.spyOn(Cookies, "getProjectId").mockReturnValue(1)
vi.spyOn(Cookies, "getToken").mockReturnValue("token")
vi.spyOn(apiUtils, "queryAndValidate")

describe("getTeams", () => {
	it("should call queryAndValidate with correct arguments", async() => {
		const data = createArray(6, i => fakeTeam({ id: i + 1 }))

		// Setup mock response
		const mockResponse = fakeResponse200(JSON.stringify(data))
		vi.mocked(fetch).mockResolvedValueOnce(mockResponse)

		// Call the function
		const teams = await getTeams()

		// Assertions
		expect(teams).toEqual(data)
		expect(apiUtils.queryAndValidate).toHaveBeenCalledWith({
			route: "teams",
			// eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
			responseSchema: expect.any(Object)
		})
	})

	it("should throw an error if the response is an error", async() => {
		// Setup mock response
		const mockResponse = fakeResponse500("Internal Server Error")
		vi.mocked(fetch).mockResolvedValueOnce(mockResponse)

		// Call the function
		const promise = getTeams()

		// Assertions
		await expect(promise).rejects.toThrow("Failed to fetch GET teams: 500 Internal Server Error")
	})

	it("should throw an error if the response is invalid", async() => {
		// Setup mock response
		const mockResponse = fakeResponse200("invalid json")
		vi.mocked(fetch).mockResolvedValueOnce(mockResponse)

		// Call the function
		const promise = getTeams()

		// Assertions
		await expect(promise).rejects.toThrow("Failed to validate GET teams")
	})
})