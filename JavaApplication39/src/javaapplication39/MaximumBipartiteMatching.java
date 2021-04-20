package javaapplication39;

import java.util.Arrays;

public class MaximumBipartiteMatching {

    static class Graph {

        int hospitals;
        int applicants;
        int adjMatrix[][];

        public Graph(int applicants, int hospitals) {
            this.hospitals = hospitals;
            this.applicants = applicants;
            adjMatrix = new int[applicants][hospitals];
        }

        public void canDoJob(int applicant, int hospital) {
            //add edge - means applicant can do this job
            adjMatrix[applicant][hospital] = 1;
        }
    }

    public int maxMatching(Graph graph) {
        int applicants = graph.applicants;
        int hospitals = graph.hospitals;

        int assign[] = new int[hospitals];    //an array to track which job is assigned to which applicant
        for (int i = 0; i < hospitals; i++) {
            assign[i] = -1;    //initially set all jobs are available
        }
        int hosCount = 0;
        int j = 0;
        for (int applicant = 0; applicant < applicants; applicant++) {    //for all applicants
            //for each applicant, all jobs will be not visited initially
            boolean visited[] = new boolean[hospitals];

            //check if applicant can get a job
            if (bipartiteMatch(graph, applicant, visited, assign)) {
                for (int i = 0; i < assign.length; i++) {
                    String hospitalName = "";
                    switch (i) {
                        case 0:
                            hospitalName = "King Abdulaziz University";
                            break;
                        case 1:
                            hospitalName = "King Fhad";
                            break;
                        case 2:
                            hospitalName = "East Jeddah";
                            break;
                        case 3:
                            hospitalName = "King Fahad Armrd Forces";
                            break;
                        case 4:
                            hospitalName = "King Faisal Specialist";
                            break;
                        case 5:
                            hospitalName = "Ministry of National Guard";
                            break;
                    }
                    String applicantName = "";
                    switch (applicant) {
                        case 0:
                            applicantName = "Ahmed";
                            break;
                        case 1:
                            applicantName = "Mahmoud";
                            break;
                        case 2:
                            applicantName = "Eman";
                            break;
                        case 3:
                            applicantName = "Fatimah";
                            break;
                        case 4:
                            applicantName = "Kamel";
                            break;
                        case 5:
                            applicantName = "Nojood";
                            break;
                    }
                    String string = "<---goes to-->";
                    if (assign[i] == j) {
                        System.out.printf("%-10s%-20s%s",applicantName,string, hospitalName);
                        System.out.println("");
                    }
                }
                //if yes then increase the job count
                j++;
                hosCount++;
            }
        }
        return hosCount;
    }

    boolean bipartiteMatch(Graph graph, int applicant, boolean visited[], int assign[]) {
        //check each job for the applicant
        for (int hospital = 0; hospital < graph.hospitals; hospital++) {
            //check if applicant can do this job means adjMatrix[applicant][job] == 1
            // and applicant has not considered for this job before, means visited[job]==false
            if (graph.adjMatrix[applicant][hospital] == 1 && !visited[hospital]) {
                //mark as job is visited, means applicant is considered for this job
                visited[hospital] = true;
                //now check if job was not assigned earlier - assign it to this applicant
                // OR job was assigned earlier to some other applicant 'X' earlier then
                //make recursive call for applicant 'X' to check if some other job can be assigned
                // so that this job can be assigned to current candidate.
                int assignedApplicant = assign[hospital];
                if (assignedApplicant == -1) {
                    assign[hospital] = applicant;    //assign job job to applicant applicant
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        //Construct Graph with applicants and jobs
        int applicants = 6;
        int hospitals = 6;
        Graph graph = new Graph(applicants, hospitals);
        graph.canDoJob(0, 0);
        graph.canDoJob(0, 1);
        graph.canDoJob(1, 5);
        graph.canDoJob(2, 0);
        graph.canDoJob(2, 3);
        graph.canDoJob(3, 2);
        graph.canDoJob(4, 3);
        graph.canDoJob(4, 4);
        graph.canDoJob(5, 5);

        MaximumBipartiteMatching m = new MaximumBipartiteMatching();
        System.out.println("\nMaximum number of applicants that could"
                + " get hospitals are: " + m.maxMatching(graph));
    }
}
