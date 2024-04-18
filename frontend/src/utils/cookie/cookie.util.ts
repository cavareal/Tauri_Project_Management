import type { CookieName } from "./cookie.type"

/**
 * Get the value of a cookie by its name
 * @param name The name of the cookie to get
 * @returns The value of the cookie if it exists, null otherwise
 */
export const getCookie = <T = string>(name: CookieName): T | null => {
	const cookies = document.cookie
	const index = cookies.search(name)

	if (index === -1) return null

	// remove the ; at the end of the cookie
	let end = cookies.indexOf(";", index)
	end = end === -1 ? cookies.length : end
	return cookies.slice(index + name.length + 1, end) as T
}

/**
 * Set a cookie with a name and a value
 * @param name The name of the cookie
 * @param value The value of the cookie
 */
export const setCookie = (name: CookieName, value: string): void => {
	document.cookie = `${name}=${value}; path=/`
}