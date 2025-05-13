export interface InstitutionInfo {
  progress: string;
  name: string;
  type: string;
  address: string;
}

export interface InstitutionForm {
  institutionName: string;
  institutionPhone: string;
  address: string;
  institutionCategoryId: number | number[] | null;
  institutionLevel: number | null;
}

export interface CategoryOption {
  value: number;
  label: string;
  children?: CategoryOption[];
}
