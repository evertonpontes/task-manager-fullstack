import { create } from 'zustand';

type Team = {
  id: string;
  name: string;
  sortIdex: number;
};

type TeamStore = {
  teams: Team[];
  setTeams: (teams: Team[]) => void;
  getTeamById: (id: string) => Team | undefined;
};

export const useTeam = create<TeamStore>((set, get) => ({
  teams: [],
  setTeams: (teams: Team[]) => set({ teams }),
  getTeamById: (id: string) => get().teams.find((team) => team.id === id),
}));
