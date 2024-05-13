import { format } from "date-fns"

export const serializeDate = (date: Date): string => {
	return format(date, "yyyy-MM-dd")
}