import { h } from "vue"
import DropdownAction from "@/components/test/DataTableDropDown.vue"

export const columns = [
	{
		accessorKey: "amount",
		header: () => h("div", { class: "text-right" }, "Amount"),
		cell: ({ row }) => {
			const amount = Number.parseFloat(row.getValue("amount"))
			const formatted = new Intl.NumberFormat("en-US", {
				style: "currency",
				currency: "USD"
			}).format(amount)

			return h("div", { class: "text-right font-medium" }, formatted)
		}
	},
	{
		id: "actions",
		enableHiding: false,
		cell: ({ row }) => {
			const payment = row.original

			return h("div", { class: "relative" }, h(DropdownAction, {
				payment
			}))
		}
	}
]