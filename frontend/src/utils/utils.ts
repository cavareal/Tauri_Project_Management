import type { ClassValue } from "clsx"
import { clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
	return twMerge(clsx(inputs))
}

export function separateStringOnFirstSpace(str: string) {
	const index = str.indexOf(" ")
	if (index === -1) {
		return [str, ""]
	}
	return [str.slice(0, index), str.slice(index + 1)]
}