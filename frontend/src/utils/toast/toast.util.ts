import { useToast } from "@/components/ui/toast"

const TOAST_DURATION = 5000

export const createToast = (message: string) => {
	const { toast } = useToast()

	toast({
		description: message,
		duration: TOAST_DURATION
	})
}