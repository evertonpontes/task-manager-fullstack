import { create } from 'zustand';

type Organization = {
  id: string;
  name: string;
  sortIdex: number;
};

type OrganizationStore = {
  organizations: Organization[];
  setOrganizations: (organizations: Organization[]) => void;
  getOrganizationById: (id: string) => Organization | undefined;
};

export const useOrganization = create<OrganizationStore>((set, get) => ({
  organizations: [],
  setOrganizations: (organizations: Organization[]) => set({ organizations }),
  getOrganizationById: (id: string) =>
    get().organizations.find((organization) => organization.id === id),
}));
