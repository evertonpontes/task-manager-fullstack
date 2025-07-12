import { Navbar } from "@/components/navbar";
import { Button } from "@/components/ui/button";
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { TabsContent } from "@radix-ui/react-tabs";
import { Plus, SquareKanban, TableProperties } from "lucide-react";

export default function TasksPage() {
    return (
        <div className="flex w-full flex-col gap-6">
            <Tabs defaultValue="TableView">
                <Navbar>
                    <Button>
                        <Plus className="mr-1/2 size-4" />
                        Add new
                    </Button>
                    <TabsList>
                        <TabsTrigger value="TableView">
                            <TableProperties className="mr-1/2 size-4 text-muted-foreground" />
                            Table view
                        </TabsTrigger>
                        <TabsTrigger value="Kanban">
                            <SquareKanban className="mr-1/2 size-4 text-muted-foreground" />
                            Kanban board
                        </TabsTrigger>
                    </TabsList>
                </Navbar>
                <TabsContent value="TableView">
                    {new Array(50).fill(0).map((_, index) => (
                        <div key={index}>Task {index}</div>
                    ))}
                </TabsContent>
                <TabsContent value="Kanban">Kanban</TabsContent>
            </Tabs>
        </div>
    );
}
